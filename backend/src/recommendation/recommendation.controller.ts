import { Body, Controller, Get, Param, Query } from '@nestjs/common';
import { RecommendationService } from './recommendation.service';
import {
  ApiOkResponse,
  ApiOperation,
  ApiQuery,
  ApiTags,
} from '@nestjs/swagger';

@ApiTags('Recommendations')
@Controller('')
export class RecommendationsController {
  constructor(private readonly service: RecommendationService) {}

  @Get('/recommendations')
  @ApiOperation({ summary: 'Get all movies (recommendations) with pagination' })
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
  async getRecommendations(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
  ) {
    return await this.service.get(page, limit);
  }

  @Get('recommendations/:id')
  @ApiOperation({ summary: 'Get all movies (recommendations) by movie' })
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
  async getRecommendationsById(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
    @Param('id') id: string,
  ) {
    return await this.service.getRecRelatedTo(page, limit, [id]);
  }

  @Get('/recommendations-related-to-id')
  @ApiOperation({ summary: 'Get all movies (recommendations) by movies' })
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
  async getRecommendationsRelatedToIds(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
    @Body() content: { movies: string[] },
  ) {
    return await this.service.getRecRelatedTo(page, limit, content.movies);
  }
}
