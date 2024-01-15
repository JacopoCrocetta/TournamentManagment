import { Module } from "@nestjs/common";
import { TournamentsInfrastructureRepository } from "./tournament/tournaments-infra.repository";

export const providers = [TournamentsInfrastructureRepository];

@Module({
    providers:[...providers],
  exports: [...providers],
})
export class RepositoryInfrastructureModule {}
