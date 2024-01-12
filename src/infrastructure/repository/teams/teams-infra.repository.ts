import { Prisma } from "@prisma/client";
import { PrismaService } from "src/infrastructure/database/prisma.service";

type matchCreateInput = Prisma.TeamsCreateInput;
type WhereUniqueInput = Prisma.TeamsWhereUniqueInput;

export interface UpdateParams {
  where: Prisma.TeamsWhereUniqueInput;
  data: Prisma.TeamsUpdateInput;
}

export interface GetManyParams {
  skip: number;
  take: number;
  cursor: Prisma.TeamsWhereUniqueInput;
  where: Prisma.TeamsWhereInput;
  orderBy: Array<Prisma.TeamsOrderByWithRelationInput>;
  include: Prisma.TeamsInclude;
}

export abstract class TeamsInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}