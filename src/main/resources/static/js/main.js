function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}


var messageApi = Vue.resource('/people{/id}');

Vue.component('add-form', {
    props: ['peoplelist', 'dudeAttr'],
    data: function() {
        return {
            name: '',
            id: ''
        }
    },
    watch: {
        dudeAttr: function(newVal, oldVal) {
            this.name = newVal.name;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="name" placeholder="Новый житель комнаты" v-model="name" />' +
        '<input type="button" value="Добавить" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            var message = { name: this.name };

            if (this.id) {
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.peoplelist, data.id);
                        this.peoplelist.splice(index, 1, data);
                        this.name = ''
                        this.id = ''
                    })
                )
            } else {
                messageApi.save({}, message).then(result =>
                    result.json().then(data => {
                        this.peoplelist.push(data);
                        this.name = ''
                    })
                )
            }
        }
    }
});

Vue.component('dude-row', {
    props: ['dude', 'editMethod', 'peoplelist'],
    template: '<div>' +
        '<i>({{ dude.id }})</i> {{ dude.name }}' +
        '<span style="position: absolute; right: 0">' +
            '<input type="button" value="Редактировать" @click="edit" />' +
            '<input type="button" value="X" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        edit: function() {
            this.editMethod(this.dude);
        },
        del: function() {
            messageApi.remove({id: this.dude.id}).then(result => {
                if (result.ok) {
                    this.peoplelist.splice(this.peoplelist.indexOf(this.dude), 1)
                }
            })
        }
    }
});

Vue.component('roommates-list', {
    props: ['peoplelist'],
    data: function() {
        return {
            dude: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<add-form :peoplelist="peoplelist" :dudeAttr="dude" />' +
        '<dude-row v-for="dude in peoplelist" :key="dude.id" :dude="dude" ' +
        ':editMethod="editMethod" :peoplelist="peoplelist" />' +
        '</div>',
    created: function() {
        messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(message => this.peoplelist.push(message))
            )
        )
    },
    methods: {
        editMethod: function(dude) {
            this.dude = dude;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<roommates-list :peoplelist="peoplelist" />',
    data: {
        peoplelist: []
    }
});