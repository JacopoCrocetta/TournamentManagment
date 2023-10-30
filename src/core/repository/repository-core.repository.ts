import { Module } from "@nestjs/common";
import { RepositoryInfrastructureModule } from "src/infrastructure/repository/repository-infrastructure.module";

@Module({imports: [RepositoryInfrastructureModule]})
export class RepositoryCoreModule{}