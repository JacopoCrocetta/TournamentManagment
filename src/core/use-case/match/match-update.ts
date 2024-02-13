import { Inject, Injectable, Logger } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

@Injectable()
export class MatchUpdate {
  constructor(@Inject() private repository: IMatchRepository) {}

  async updateTournament(inputToUpdate: Match): Promise<Match> {
    Logger.log("UPDATING MATCH WITH TIME " + inputToUpdate.matchTime + "...");
    const matchUpdated = this.repository.update(inputToUpdate);

    Logger.log(inputToUpdate.id + " UPDATED ON "+ inputToUpdate);
    return matchUpdated;
  }
}
