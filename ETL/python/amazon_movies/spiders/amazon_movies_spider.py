from __future__ import with_statement
from amazon_movies.items import AmazonMoviesItem
from urlparse import urlparse
import scrapy , urllib2 , re


class AmazonMoviesSpider(scrapy.Spider) :
	name = 'amazon_movies_spider'
	allowed_domins = ['www.amazon.com']
	start_urls = [
		'http://www.amazon.com/dp/B00241EKL8' ,
	]

	def parse(self , response) :
		#fetch products id
		products = []
		with open('movies.txt' , 'r' , -1) as results :
			while True :
				line = results.readline()
				if not line : 
					break
				products.append(line.strip())

		#products = ['B00241EKL8' , 'B000F9T6ZG']
		#products = products[700:750]

		#fetch movie infos
		for sites in products :
			url = 'http://www.amazon.com/dp/' + sites
			request = urllib2.Request(url)
			yield scrapy.Request(url , callback = self.parse_movie_title)


	def parse_movie_title(self , response) :
		res = AmazonMoviesItem()

		## Movie product id
		res['movie_productId'] = (response.url)[(response.url).rfind('/') + 1 : ]

		## Movie title
		title_pattern = r'http://www.amazon.com/(.[^/]*)/(.*)'
		res['movie_title'] = response.css('head link[rel="canonical"]::attr(href)').extract()[0]
		tmp = re.match(title_pattern , res['movie_title'])
		if tmp != None :
			res['movie_title'] = tmp.group(1).strip()
		else :
			res['movie_title'] = ''

		
		## Movie details
		if response.css('body[id="dv-product-details"]') :
			tag_list = response.css('body[id="dv-product-details"] #a-page div div div table tr th::text').extract()
			details_list = response.css('body[id="dv-product-details"] #a-page div div div table tr td')
			if details_list :
				# product type
				res['product_type'] = 0

				tag_cnt = 0
				for tag in tag_list :
					if tag.strip() == 'Genres' :
						# Movie genres
						genres = ''
						for genre in details_list[tag_cnt].css('a::text') :
							genres += genre.extract()
							genres += '|'
						if genres != '' :
							res['movie_genres'] = genres[:-1]
						else :
							res['movie_genres'] = genres

					elif tag.strip() == 'Director' :
						# Movie directors
						directors = ''
						for director in details_list[tag_cnt].css('a::text') :
							directors += director.extract()
							directors += '|'
						if directors != '' :
							res['movie_directors'] = directors[:-1]
						else :
							res['movie_directors'] = directors

					elif tag.strip() == 'Starring' :
						# Movie starrings
						starrings = ''
						for star in details_list[tag_cnt].css('a::text') :
							starrings += star.extract()
							starrings += '|'
						if starrings != '' :
							res['movie_stars'] = starrings[:-1]
						else :
							res['movie_stars'] = starrings		

					elif tag.strip() == 'Supporting actors' :
						# Movie supporting actors
						supportings = ''
						for sup in details_list[tag_cnt].css('a::text') :
							supportings += sup.extract()
							supportings += '|'
						if supportings != '' :
							res['movie_supporting_actors'] = supportings[:-1]
						else :
							res['movie_supporting_actors'] = supportings		

					elif tag.strip() == 'Studio' :
						# Movie studio
						studios = ''
						for studio in details_list[tag_cnt].css('::text').extract() :
							studios += studio.strip()
							studios += '|'
						if studios != '' :
							res['movie_studio'] = studios[:-1]
						else :
							res['movie_studio'] = studios

					elif tag.strip() == 'MPAA rating' :
						# Movie MPAA
						MPAAs = ''
						for mpaa in details_list[tag_cnt].css('::text').extract() :
							MPAAs += mpaa.strip()
							MPAAs += '|'
						if MPAAs != '' :
							res['movie_MPAA'] = MPAAs[:-1]
						else :
							res['movie_MPAA'] = MPAAs

					elif tag.strip() == 'Format' :
						# Movie format
						formats = ''
						for form in details_list[tag_cnt].css('::text').extract() :
							formats += form.strip()
							formats += '|'
						if formats != '' :
							res['movie_version'] = formats[:-1]
						else :
							res['movie_version'] = formats

					tag_cnt += 1

				# Movie release time
				if response.css('span.release-year::text') :
					release_time = ''
					for time in response.css('span.release-year::text').extract() :
						release_time += time.strip()
						release_time += '|'
					if release_time != '' and (len(release_time) < 64) :
						res['movie_release_time'] = release_time[:-1]
					else :
						res['movie_release_time'] = release_time

				# Movie run time
				if response.css('dl.dv-meta-info dt::text') :
					runtimetaglist = response.css('dl.dv-meta-info dt::text').extract()
					tag_cnt = 0
					for tag in runtimetaglist :
						if tag.strip() == 'Runtime:' :
							for rt in response.css('dl.dv-meta-info dd')[tag_cnt].css('::text').extract() :
								res['movie_runtime'] = rt.strip()
							break
						tag_cnt += 1

			else :
				res['product_type'] = -1
				#res['movie_genres'] =''
				#res['movie_directors'] = ''
				#res['movie_stars'] = ''
				#res['movie_supporting_actors'] = ''
				#res['movie_studio'] = ''
				#res['movie_MPAA'] = ''
				#res['movie_version'] = ''
				#res['movie_release_time'] = ''

		elif response.css('body[id="dp"]') :
			tag_list = response.css('#detail-bullets table tr td.bucket div.content ul li b:first-child::text').extract()
			details_list = response.css('#detail-bullets table tr td.bucket div.content ul li')

			if tag_list :
				tmp_cnt = 0
				for tag in tag_list :
					if tag.strip() == 'Actors:' :
						## Movie actors
						actors = ''
						for actor in details_list[tmp_cnt].css('a::text').extract() :
							actors += actor.strip()
							actors += '|'

						if actors != '' :
							res['movie_supporting_actors'] = actors[:-1]
						else :
							res['movie_supporting_actors'] = actors

					elif tag.strip() == 'Directors:' :
						## Movie directors
						directors = ''
						for director in details_list[tmp_cnt].css('a::text').extract() :
							directors += director.strip()
							directors += '|'
						if directors != '' :
							res['movie_directors'] = directors[:-1]
						else :
							res['movie_directors'] = directors

					elif tag.strip() == 'Format:' :
						## Movie formats
						formats = ''
						for form in details_list[tmp_cnt].css(':not(b)::text').extract() :
							formats += form.strip()
							formats += '|'
						if formats != '' :
							res['movie_version'] = formats[1:-1]
						else :
							res['movie_version'] = formats
						
					elif tag.strip() == 'Studio:' :
						## Movie studio 
						studios = ''
						for studio in details_list[tmp_cnt].css(':not(b)::text').extract() :
							studios += studio.strip()
							studios += '|'
						if studios != '' :
							res['movie_studio'] = studios[1:-1]
						else :
							res['movie_studio'] = studios

					elif tag.strip() == 'DVD Release Date:' :
						## Product release date
						time = ''
						for t in details_list[tmp_cnt].css(':not(b)::text').extract() :
							time += t.strip()
							time += '|'
						if time != '' and (len(time) < 64):
							res['movie_release_time'] = time[1:-1]
						else :
							res['movie_release_time'] = time

					elif tag.strip() == 'VHS Release Date:' :
						## Product release date
						time = ''
						for t in details_list[tmp_cnt].css(':not(b)::text').extract() :
							time += t.strip()
							time += '|'
						if time != '' and (len(time) < 64):
							res['movie_release_time'] = time[1:-1]
						else :
							res['movie_release_time'] = time

					elif tag.strip() == 'Rated:' :
						rates = ''	
						for r in details_list[tmp_cnt].css('span span::text').extract() :
							rates += r.strip()
							rates += '|'
						if rates != '' :
							res['movie_MPAA'] = rates[:-1]
						else :
							res['movie_MPAA'] = rates
					elif tag.strip() == 'Run Time:' :
						rtime = ''
						for rt in details_list[tmp_cnt].css(':not(b)::text').extract() :
							rtime += rt.strip()
							rtime += '|'
						if rtime != '' :
							res['movie_runtime'] = rtime[1:-1]
						else :
							res['movie_runtime'] = rtime

					tmp_cnt = tmp_cnt + 1

			else :
				res['product_type'] = -1
		
		else :
			res['product_type'] = -1

		yield res
