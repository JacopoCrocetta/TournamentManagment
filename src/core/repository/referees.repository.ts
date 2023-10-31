import { Referees } from "../entity/abstract-referees";

export interface IRefereesRepository {
  getById(id: number): Referees;
  getAll(): Referees[];

  delete(id: number): void;
}
