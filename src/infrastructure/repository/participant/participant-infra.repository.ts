import { PrismaService } from "src/infrastructure/database/prisma.service";

export abstract class ParticipantInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}