import { Inject, Injectable } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

@Injectable()
export class MatchCreate {
  constructor(@Inject() private matchRepository: IMatchRepository) {}

  create = async (matchToCreate: Match): Promise<Match> => {
    return await this.matchRepository.create(matchToCreate);
  };

  update = async (matchToUpdate: Match):Promise<Match> =>{
    return await this.matchRepository.update(matchToUpdate);
  }
}
