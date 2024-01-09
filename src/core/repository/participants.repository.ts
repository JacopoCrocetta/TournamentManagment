import { Participants } from "../entity/abstract-participants";


export interface IParticipantsRepository {
  getById(id: number): Participants;
  getAllByIdTournament(idTournament: number): Participants[];

  create(matchToCreate: Participants): Promise<Participants>;
  update(matchToUpdate: Participants): Promise<Participants>;

  delete(id: number): boolean;
}
