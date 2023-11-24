import { Module } from "@nestjs/common";
import { AppController } from "./infrastructure/controller/healtz.controller";
import { PrismaService } from "./infrastructure/database/prisma.service";
import { ConfigModule } from "@nestjs/config";
import { TournamentController } from "./infrastructure/controller/tournament.controller";
import { PresenterModule } from "./infrastructure/presenter/presenter.module";

@Module({
    imports:[ConfigModule.forRoot()],
    controllers:[AppController, TournamentController],
    providers: [PrismaService, PresenterModule]
})
export class AppModule{}