import { PrismaService } from "src/infrastructure/database/prisma.service";


export abstract class TournamentsRepository {
  constructor(protected prismaService:PrismaService){}


  
}
