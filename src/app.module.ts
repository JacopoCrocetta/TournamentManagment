import { Module } from "@nestjs/common";
import { AppController } from "./infrastructure/controller/healtz.controller";
import { TournamentController } from "./infrastructure/controller/tournament.controller";
import { PresenterModule } from "./infrastructure/presenter/presenter.module";
import { UseCasesModule } from "./core/use-case/use-cases.module";
import { RepositoryCoreModule } from "./core/repository/repository-core.repository";
import { RepositoryInfrastructureModule } from "./infrastructure/repository/repository-infrastructure.module";


const providers = [ PresenterModule, UseCasesModule, RepositoryCoreModule, RepositoryInfrastructureModule];
const controllers = [AppController, TournamentController];

@Module({
    imports: [PresenterModule, UseCasesModule, RepositoryCoreModule],
    controllers:[...controllers],
    providers: [...providers]
})
export class AppModule{}