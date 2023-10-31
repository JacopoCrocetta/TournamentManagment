import { Match } from "../entity/abstract-match";

export interface IMatchRepository {
  getById(id: number): Match;
  getAll(): Match[];

  delete(id: number): void;
}
