import { PrismaService } from "src/infrastructure/database/prisma.service";

export abstract class RefereesInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}