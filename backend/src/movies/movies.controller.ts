import {
  Body,
  Controller,
  Get,
  HttpCode,
  Param,
  Post,
  Query,
} from '@nestjs/common';
import {
  ApiBody,
  ApiNotFoundResponse,
  ApiOkResponse,
  ApiOperation,
  ApiQuery,
  ApiTags,
} from '@nestjs/swagger';
import { MoviesService } from './movies.service';

@ApiTags('Movies')
@Controller('movies')
export class MoviesController {
  constructor(private readonly moviesService: MoviesService) {}

  @Get()
  @ApiOperation({ summary: 'Get all movies with pagination' })
  @ApiQuery({
    name: 'page',
    type: Number,
    required: false,
    description: 'Page number',
  })
  @ApiQuery({
    name: 'limit',
    type: Number,
    required: false,
    description: 'Items per page',
  })
  @ApiOkResponse({
    description: 'Returns all movies with pagination information',
    schema: {
      properties: {
        data: {
          type: 'array',
          items: { type: 'object', properties: {} },
        },
        pagination: {
          type: 'object',
          properties: {
            total: { type: 'number' },
            page: { type: 'number' },
            limit: { type: 'number' },
            pages: { type: 'number' },
          },
        },
      },
    },
  })
  findAll(@Query('page') page: number = 1, @Query('limit') limit: number = 10) {
    return this.moviesService.findAll(page, limit);
  }

  @Get('favorites')
  @ApiOperation({ summary: 'Get all favorite movies for a user' })
  @ApiQuery({
    name: 'userId',
    type: String,
    required: true,
    description: 'User ID',
  })
  @ApiQuery({
    name: 'page',
    type: Number,
    required: false,
    description: 'Page number',
  })
  @ApiQuery({
    name: 'limit',
    type: Number,
    required: false,
    description: 'Items per page',
  })
  @ApiOkResponse({
    description: 'Returns all favorite movies with pagination information',
    schema: {
      type: 'object',
      properties: {
        data: {
          type: 'array',
          items: {
            type: 'object',
            properties: {
              user_id: { type: 'string' },
              movie_id: { type: 'string' },
              title: { type: 'string' },
            },
          },
        },
        pagination: {
          type: 'object',
          properties: {
            total: { type: 'number' },
            page: { type: 'number' },
            limit: { type: 'number' },
            pages: { type: 'number' },
          },
        },
      },
    },
  })
  @ApiNotFoundResponse({ description: 'No favorites found for this user' })
  findAllFavorites(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
  ) {
    return this.moviesService.findAllFavorites(page, limit);
  }

  @Get(':id')
  // @ApiOperation({ summary: 'Get a movie by id' })
  // @ApiParam({ name: 'id', description: 'Movie ID' })
  // @ApiOkResponse({ description: 'Returns the movie', type: Movie })
  // @ApiNotFoundResponse({ description: 'Movie not found' })
  findOne(@Param('id') id: string) {
    return this.moviesService.findOne(id);
  }

  @Post('favorites')
  @ApiOperation({ summary: 'Add a movie to favorites' })
  @ApiBody({
    description: 'Movie ID and User ID',
    schema: {
      properties: {
        movieId: { type: 'string', description: 'Movie ID to favorite' },
        userId: { type: 'string', description: 'User ID' },
      },
    },
  })
  @ApiOkResponse({ description: 'Movie added to favorites successfully' })
  @HttpCode(200)
  async setFavorites(@Body() body: { movieId: string }) {
    const { movieId } = body;
    await this.moviesService.setFavorite(movieId);
  }
}
