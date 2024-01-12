import { PrismaService } from "src/infrastructure/database/prisma.service";

export abstract class StandingsInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}