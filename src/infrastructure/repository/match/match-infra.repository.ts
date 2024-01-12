import { PrismaService } from "src/infrastructure/database/prisma.service";

export abstract class MatchInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}