import { Tournament } from "../entity/abstract-tournament";

export interface ITournamentsRepository {
  getById(id: number): Tournament;
  getAll(): Tournament[];

  delete(id: number): void;
}
