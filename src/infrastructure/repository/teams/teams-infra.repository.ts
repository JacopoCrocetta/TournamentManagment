import { PrismaService } from "src/infrastructure/database/prisma.service";



export abstract class TeamsInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}