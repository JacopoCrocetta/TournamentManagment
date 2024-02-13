import { Tournament } from "../entity/abstract-tournament";

export const TOURNAMENT_REPOSITORY = "TournamentInfrastructureRepository";

export interface ITournamentsRepository {
  getById(id: string);
  getAll();

  create(matchToCreate: Tournament);
  update(matchToUpdate: Tournament);

  delete(id: string);
}
