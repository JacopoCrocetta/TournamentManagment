import { Injectable } from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { PrismaService } from "src/infrastructure/database/prisma.service";

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
  include: Prisma.TournamentsInclude;
}

@Injectable()
export abstract class TournamentsBaseRepository {
  protected constructor(protected prismaService: PrismaService) {}

  /**
   * Retrieves a tournament by its unique identifier.
   * @param {WhereUniqueInput} where - The unique identifier of the tournament.
   * @returns {Promise} - A promise that resolves with the retrieved tournament.
   */
  protected _getById = async (where: WhereUniqueInput) => {
    return this.prismaService.tournaments.findUnique({ where });
  };

  /**
   * Retrieves all tournaments.
   * @returns {Promise} - A promise that resolves with an array of all tournaments.
   */
  protected _getAll = async () => {
    return this.prismaService.tournaments.findMany();
  };

  /**
   * Deletes a tournament.
   * @param {WhereUniqueInput} where - The unique identifier of the tournament to be deleted.
   * @returns {Promise} - A promise that resolves when the tournament is deleted.
   */
  protected _delete = async (where: WhereUniqueInput) =>
    this.prismaService.tournaments.delete({ where });

  /**
   * Retrieves multiple tournaments based on specified parameters.
   * @param {GetManyParams} params - The parameters for retrieving multiple tournaments.
   * @returns {Promise} - A promise that resolves with an array of retrieved tournaments.
   */
  protected _getMany = async (params: GetManyParams) => {
    const { skip, take, cursor, where, orderBy, include } = params;
    return this.prismaService.tournaments.findMany({
      skip,
      take,
      cursor,
      where,
      orderBy,
      include,
    });
  };
}
