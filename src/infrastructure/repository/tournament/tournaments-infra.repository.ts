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

export class TournamentsInfrastructureRepository extends TournamentsBaseRepository implements ITournamentsRepository {
  /**
   * Constructor for creating a new instance of the class.
   *
   * @param {PrismaService} prismaService - the Prisma service to be used
   * @return {void} 
   */
  constructor(protected prismaService:PrismaService){
    super(prismaService)
  }

  /**
   * Retrieves an item by its ID.
   *
   * @param {string} id - the ID of the item to retrieve
   * @return {ReturnType} the retrieved item
   */
  getById(id: string) {
    return this._getById({id});
  }
  /**
   * Retrieves all items.
   *
   * @return {type} description of return value
   */
  getAll(){
    return this._getAll();
  }

  /**
   * Create a match to be added to the tournament.
   *
   * @param {Tournament} matchToCreate - the match to be created
   * @return {void} 
   */
  create(matchToCreate: Tournament){
    return;
  }

  /**
   * Update the match to be the provided tournament.
   *
   * @param {Tournament} matchToUpdate - the tournament to be set as the match
   * @return {void} 
   */
  update(matchToUpdate: Tournament){
    return;
  }

  /**
   * Deletes an item by its ID.
   *
   * @param {string} id - the ID of the item to delete
   * @return {void} 
   */
  delete(id: string) {
    return;
  }
}
