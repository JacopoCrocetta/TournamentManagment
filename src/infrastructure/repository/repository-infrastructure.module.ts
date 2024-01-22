import { Module } from "@nestjs/common";
import { TournamentsInfrastructureRepository } from "./tournament/tournaments-infra.repository";
import { PrismaService } from "../database/prisma.service";

const providers = [PrismaService, TournamentsInfrastructureRepository];

@Module({
  imports: [],
  providers,
  exports: [...providers],
})
export class RepositoryInfrastructureModule {}
