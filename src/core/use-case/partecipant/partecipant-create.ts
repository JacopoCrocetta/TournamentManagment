import { Inject, Injectable } from "@nestjs/common";
import { Participants } from "src/core/entity/abstract-participants";
import { IParticipantsRepository } from "src/core/repository/participants.repository";


@Injectable()
export class PartecipantCreate {
  constructor(@Inject() private participantsRepository: IParticipantsRepository) {}

  update = async (input:Participants):Promise<Participants> =>{
    return await this.participantsRepository.update(input);
  }

  create = async (input: Participants):Promise<Participants> => {
    return await this.participantsRepository.create(input);
  };
}
