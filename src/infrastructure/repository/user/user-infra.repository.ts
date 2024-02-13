import { PrismaService } from "src/infrastructure/database/prisma.service";
import { Prisma } from "@prisma/client";

type userCreateInput = Prisma.UsersCreateInput;
type WhereUniqueInput = Prisma.UsersWhereUniqueInput;

export interface UpdateParams {
  where: Prisma.UsersWhereUniqueInput;
  data: Prisma.UsersUpdateInput;
}

export interface GetManyParams {
  skip: number;
  take: number;
  cursor: Prisma.TournamentsWhereUniqueInput;
  where: Prisma.TournamentsWhereInput;
  orderBy: Array<Prisma.TournamentsOrderByWithRelationInput>;
  include: Prisma.TournamentsInclude;
}

export abstract class UserInfrastructureRepository {
  constructor(protected prismaService: PrismaService) {}
}
