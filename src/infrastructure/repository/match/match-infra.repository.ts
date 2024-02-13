import { Prisma } from "@prisma/client";
import { PrismaService } from "src/infrastructure/database/prisma.service";

type matchCreateInput = Prisma.MatchesCreateInput;
type WhereUniqueInput = Prisma.MatchesWhereUniqueInput;

export interface UpdateParams {
  where: Prisma.MatchesWhereUniqueInput;
  data: Prisma.MatchesUpdateInput;
}

export interface GetManyParams {
  skip: number;
  take: number;
  cursor: Prisma.MatchesWhereUniqueInput;
  where: Prisma.MatchesWhereInput;
  orderBy: Array<Prisma.MatchesOrderByWithRelationInput>;
  include: Prisma.MatchesInclude;
}

export abstract class MatchInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}