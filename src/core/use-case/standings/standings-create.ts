import { Inject, Injectable } from "@nestjs/common";
import { IStandingRepository } from "src/core/repository/standings.repository";

interface CreateStandingsInput {}

@Injectable()
export class StandingsCreate {
  constructor(@Inject() standingsRepository: IStandingRepository) {}

  create = async (input: CreateStandingsInput) => {
    return null;
  };
}
