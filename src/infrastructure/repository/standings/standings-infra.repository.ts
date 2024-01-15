import { Prisma } from "@prisma/client";
import { PrismaService } from "src/infrastructure/database/prisma.service";

type standingsCreateInput = Prisma.StandingsCreateInput;
type WhereUniqueInput = Prisma.StandingsWhereUniqueInput;

export interface UpdateParams {
  where: Prisma.StandingsWhereUniqueInput;
  data: Prisma.StandingsUpdateInput;
}

export interface GetManyParams {
  skip: number;
  take: number;
  cursor: Prisma.StandingsWhereUniqueInput;
  where: Prisma.StandingsWhereInput;
  orderBy: Array<Prisma.StandingsOrderByWithRelationInput>;
  include: Prisma.StandingsInclude;
}

export abstract class StandingsInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}