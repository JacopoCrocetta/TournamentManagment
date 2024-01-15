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
  include: Prisma.TournamentsInclude
}


@Injectable()
export abstract class TournamentsBaseRepository {
    protected constructor(protected prismaService: PrismaService) {}

    protected _create = async (data:tournamentCreateInput) => {
        return this.prismaService.tournaments.create({data});
      }
    
      protected _update = async (params:UpdateParams) => {
        const {where, data} = params;
        return this.prismaService.tournaments.update({data, where})
      }
    
      protected _getById = async (where: WhereUniqueInput) => {
        return this.prismaService.tournaments.findUnique({where})
      }
    
      protected _getAll = async () => {
        return this.prismaService.tournaments.findMany();
      }
    
      protected _delete = async (where: WhereUniqueInput) => this.prismaService.tournaments.delete({ where });
    
      protected _getMany = async (params:GetManyParams) => {
        const { skip, take, cursor, where, orderBy, include } = params;
        return this.prismaService.tournaments.findMany({skip, take, cursor, where, orderBy, include})
      }

}