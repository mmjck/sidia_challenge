import { Body, Controller, Get, Param, Query } from '@nestjs/common';
import { RecommendationService } from './recommendation.service';

@Controller('')
export class RecommendationsController {
  constructor(private readonly service: RecommendationService) {}

  @Get('/recommendations')
  async getRecommendations(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
  ) {
    return await this.service.get(page, limit);
  }

  @Get('recommendations/:id')
  async getRecommendationsById(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
    @Param('id') id: string,
  ) {
    return await this.service.getRecRelatedTo(page, limit, [id]);
  }

  @Get('/recommendations-related-to-id')
  async getRecommendationsRelatedToIds(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
    @Body() content: { movies: string[] },
  ) {
    return await this.service.getRecRelatedTo(page, limit, content.movies);
  }
}
