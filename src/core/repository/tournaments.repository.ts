import { Tournament } from "../entity/abstract-tournament";

export interface ITournamentsRepository {
  getById(id: number): Tournament;
  getAll(): Tournament[];

  create(matchToCreate: Tournament): Promise<Tournament>;
  update(matchToUpdate: Tournament): Promise<Tournament>;

  delete(id: number): boolean;
}
