import { Injectable } from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";
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

export abstract class TournamentsInfrastructureRepository  {
  constructor(protected prismaService:PrismaService){}


  protected create = async (data:tournamentCreateInput) => {
    return this.prismaService.tournaments.create({data});
  }

  protected update = async (params:UpdateParams) => {
    const {where, data} = params;
    return this.prismaService.tournaments.update({data, where})
  }

  protected getById = async (where: WhereUniqueInput) => {
    return this.prismaService.tournaments.findUnique({where})
  }

  protected _delete = async (where: WhereUniqueInput) => this.prismaService.tournaments.delete({ where });

  protected getMany = async (params:GetManyParams) => {
    const { skip, take, cursor, where, orderBy, include } = params;
    return this.prismaService.tournaments.findMany({skip, take, cursor, where, orderBy, include})
  }
}
