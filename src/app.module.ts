import { Module } from "@nestjs/common";
import { AppController } from "./infrastructure/controller/healtz.controller";
import { PrismaService } from "./infrastructure/database/prisma.service";
import { TournamentController } from "./infrastructure/controller/tournament.controller";
import { PresenterModule } from "./infrastructure/presenter/presenter.module";
import { UseCasesModule } from "./core/use-case/use-cases.module";


const providers = [PrismaService, PresenterModule, UseCasesModule];
const controllers = [AppController, TournamentController];

@Module({
    imports: [PresenterModule, UseCasesModule],
    controllers:[...controllers],
    providers: []
})
export class AppModule{}