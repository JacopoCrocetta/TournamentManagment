import { Teams } from "../entity/abstract-teams";

export interface ITeamsRepository {
  getById(id: number): Teams;
  getAll(): Teams[];

  create(matchToCreate: Teams): Promise<Teams>;
  update(matchToUpdate: Teams): Promise<Teams>;

  delete(id: number): void;
}
