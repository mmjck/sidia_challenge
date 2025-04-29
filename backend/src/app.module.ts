import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ScheduleModule } from '@nestjs/schedule';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MoviesModule } from './movies/movies.module';
import { RecommendationModule } from './recommendation/recommendation.module';

@Module({
  imports: [
    MongooseModule.forRoot(
      'mongodb://admin:123@localhost:27017/tmdb?authSource=admin',
    ),

    ScheduleModule.forRoot(),
    RecommendationModule,
    MoviesModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
