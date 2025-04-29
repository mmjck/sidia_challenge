import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { MovieDatabase, MovieDatabaseSchema } from 'src/schema/database.schema';
import { Favorites, FavoritesSchema } from '../schema/favorites.schema';
import { RecommendationsController } from './recommendation.controller';
import { RecommendationService } from './recommendation.service';
import { TasksService } from '../task/task.service';
import { Movie, MovieSchema } from 'src/schema/movies.schema';
import {
  MoviesRecommendations,
  MoviesRecommendationsSchema,
} from 'src/schema/movies-recommendation.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: MovieDatabase.name, schema: MovieDatabaseSchema },
      { name: Movie.name, schema: MovieSchema },
      { name: Favorites.name, schema: FavoritesSchema },
      { name: MoviesRecommendations.name, schema: MoviesRecommendationsSchema },
      { name: Favorites.name, schema: FavoritesSchema },
    ]),
  ],
  controllers: [RecommendationsController],
  providers: [RecommendationService, TasksService],
})
export class RecommendationModule {}
