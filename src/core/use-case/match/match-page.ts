import { Inject } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

export class MatchPage {
  constructor(@Inject() private matchRepository: IMatchRepository) {}

  getById = async (id: number): Promise<Match> => {
    return this.matchRepository.getById(id);
  };

  getAll = async (id: number): Promise<Match[]> => {
    return this.matchRepository.getAllByTournamentId(id);
  };
}
