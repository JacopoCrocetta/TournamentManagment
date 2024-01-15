import { Prisma } from "@prisma/client";
import { PrismaService } from "src/infrastructure/database/prisma.service";

type refereesCreateInput = Prisma.RefereesCreateInput;
type WhereUniqueInput = Prisma.RefereesWhereUniqueInput;

export interface UpdateParams {
  where: Prisma.RefereesWhereUniqueInput;
  data: Prisma.RefereesUpdateInput;
}

export interface GetManyParams {
  skip: number;
  take: number;
  cursor: Prisma.RefereesWhereUniqueInput;
  where: Prisma.RefereesWhereInput;
  orderBy: Array<Prisma.RefereesOrderByWithRelationInput>;
  include: Prisma.RefereesInclude;
}

export abstract class RefereesnfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}