import { Match } from "../entity/abstract-match";

export interface IMatchRepository {
  getById(id: number): Match;
  getAllByTournamentId(tournamentId: number): Match[];

  create(matchToCreate: Match): Promise<Match>;
  update(matchToUpdate: Match): Promise<Match>;

  delete(id: number): void;
}
