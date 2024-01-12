import { PrismaService } from "src/infrastructure/database/prisma.service";

export abstract class RefereesnfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}