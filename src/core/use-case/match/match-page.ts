import { Inject, Injectable, Logger } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

@Injectable()
export class MatchPage {
  constructor(@Inject() private matchRepository: IMatchRepository) {}

  getById = async (id: number): Promise<Match> => {
    Logger.log("RETRIEVING TOURNAMENT WITH ID " + id + "...");
    return this.matchRepository.getById(id);
  };

  getAll = async (id: number): Promise<Match[]> => {
    Logger.log("RETRIEVING ALL TOURNAMENT WITH ID " + id + "...");
    return this.matchRepository.getAllByTournamentId(id);
  };
}
