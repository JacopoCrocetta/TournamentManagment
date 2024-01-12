import { PrismaService } from "src/infrastructure/database/prisma.service";

export abstract class UserInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}