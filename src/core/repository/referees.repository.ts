import { Referees } from "../entity/abstract-referees";

export interface IRefereesRepository {
  getById(id: number): Referees;
  getAll(): Referees[];

  create(matchToCreate: Referees): Promise<Referees>;
  update(matchToUpdate: Referees): Promise<Referees>;

  delete(id: number): boolean;
}
