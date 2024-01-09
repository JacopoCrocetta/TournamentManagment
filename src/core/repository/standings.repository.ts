import { Standings } from "../entity/abstract-standings";

export interface IStandingRepository {
  getAllByIdTournament(idTournament: number): Standings;
  getById(id: number):Standings;

  create(matchToCreate: Standings): Promise<Standings>;
  update(matchToUpdate: Standings): Promise<Standings>;

  delete(id: number):boolean;
}
