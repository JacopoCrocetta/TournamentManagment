import { Prisma } from "@prisma/client";
import { PrismaService } from "src/infrastructure/database/prisma.service";
import { TournamentsBaseRepository } from "./tournaments-base.repository";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";
import { Tournament } from "src/core/entity/abstract-tournament";

type tournamentCreateInput = Prisma.TournamentsCreateInput;
type WhereUniqueInput = Prisma.TournamentsWhereUniqueInput;

export interface UpdateParams {
  where: Prisma.TournamentsWhereUniqueInput;
  data: Prisma.TournamentsUpdateInput;
}

export interface GetManyParams {
  skip: number;
  take: number;
  cursor: Prisma.TournamentsWhereUniqueInput;
  where: Prisma.TournamentsWhereInput;
  orderBy: Array<Prisma.TournamentsOrderByWithRelationInput>;
  include: Prisma.TournamentsInclude
}

export abstract class TournamentsInfrastructureRepository extends TournamentsBaseRepository implements ITournamentsRepository {
  constructor(protected prismaService:PrismaService){
    super(prismaService)
  }

  getById(id: string) {
    return this._getById({id});
  }
  getAll(){
    return;
  }

  create(matchToCreate: Tournament){
    return;
  }

  update(matchToUpdate: Tournament){
    return;
  }

  delete(id: string) {
    return;
  }
}
