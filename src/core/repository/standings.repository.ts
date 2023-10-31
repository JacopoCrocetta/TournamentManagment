import { Standings } from "../entity/abstract-standings";

export interface IStandingRepository {
  getAllByIdTournament(idTournament: number): Standings;
  getById(id: number):Standings;

  delete(id: number):void;
}
