import { Participants } from "../entity/abstract-participants";


export interface IParticipantsRepository {
  getById(id: number): Participants;
  getAll(): Participants[];

  create(matchToCreate: Participants): Promise<Participants>;
  update(matchToUpdate: Participants): Promise<Participants>;

  delete(id: number): void;
}
