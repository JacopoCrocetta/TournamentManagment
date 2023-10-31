import { Participants } from "../entity/abstract-participants";


export interface IParticipantsRepository {
  getById(id: number): Participants;
  getAll(): Participants[];

  delete(id: number): void;
}
