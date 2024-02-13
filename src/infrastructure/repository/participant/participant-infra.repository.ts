import { Prisma } from "@prisma/client";
import { PrismaService } from "src/infrastructure/database/prisma.service";

type participantsCreateInput = Prisma.ParticipantsCreateInput;
type WhereUniqueInput = Prisma.ParticipantsWhereUniqueInput;

export interface UpdateParams {
  where: Prisma.ParticipantsWhereUniqueInput;
  data: Prisma.ParticipantsUpdateInput;
}

export interface GetManyParams {
  skip: number;
  take: number;
  cursor: Prisma.ParticipantsWhereUniqueInput;
  where: Prisma.ParticipantsWhereInput;
  orderBy: Array<Prisma.ParticipantsOrderByWithRelationInput>;
  include: Prisma.ParticipantsInclude;
}

export abstract class ParticipantInfrastructureRepository  {
    constructor(protected prismaService:PrismaService){}
}