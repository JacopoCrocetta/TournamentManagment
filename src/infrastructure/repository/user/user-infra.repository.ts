import { PrismaService } from "src/infrastructure/database/prisma.service";
import { Prisma } from "@prisma/client";

export abstract class UserInfrastructureRepository {
  constructor(protected prismaService: PrismaService) {}
}
