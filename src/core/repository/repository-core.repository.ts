import { Module } from "@nestjs/common";
import { TOURNAMENT_REPOSITORY } from "./tournaments.repository";
import { TournamentsInfrastructureRepository } from "src/infrastructure/repository/tournament/tournaments-infra.repository";
import { RepositoryInfrastructureModule } from "src/infrastructure/repository/repository-infrastructure.module";

const providers = [
    {provide: TOURNAMENT_REPOSITORY, useExisting: TournamentsInfrastructureRepository}
];

@Module({imports: [RepositoryInfrastructureModule],
providers,
exports: []})
export class RepositoryCoreModule{}