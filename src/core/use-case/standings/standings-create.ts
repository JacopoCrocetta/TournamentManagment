import { Inject, Injectable } from "@nestjs/common";
import { Standings } from "src/core/entity/abstract-standings";
import { IStandingRepository } from "src/core/repository/standings.repository";

@Injectable()
export class StandingsCreate {
  constructor(@Inject() private standingsRepository: IStandingRepository) {}

  create = async (input: Standings) => {
    return this.standingsRepository.create(input);
  };
}
