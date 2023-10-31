import { Teams } from "../entity/abstract-teams";

export interface ITeamsRepository {
  getById(id: number): Teams;
  getAll(): Teams[];

  delete(id: number): void;
}
