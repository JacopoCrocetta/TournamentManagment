import { Module } from "@nestjs/common";
import { ControllerModule } from "./infrastructure/controller/controller.module";
import { AppController } from "./infrastructure/controller/healtz.controller";
import { PrismaService } from "./infrastructure/database/prisma.service";
import { TournamentController } from "./infrastructure/controller/tournament.controller";

@Module({
    imports:[ControllerModule],
    controllers:[AppController, TournamentController],
    providers: [PrismaService]
})
export class AppModule{}