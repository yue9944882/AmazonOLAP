# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class AmazonMoviesItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    movie_productId = scrapy.Field()

    movie_title = scrapy.Field()

    movie_stars = scrapy.Field()
    movie_directors = scrapy.Field()
    movie_supporting_actors = scrapy.Field()

    movie_genres = scrapy.Field()
    movie_release_time = scrapy.Field()
    movie_version = scrapy.Field()

    movie_studio = scrapy.Field()
    movie_MPAA = scrapy.Field()

    movie_runtime = scrapy.Field()

    product_type = scrapy.Field() ## product type , 0 stands for movies , 1 stands for other product types
